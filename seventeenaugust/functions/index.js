/*
All versions of the required npm modules are mentioned in 
'package.json'.

'firebase-functions'  for rendering https onRequest method.
'firebase-admin' for database references and cloud messaging functions.
'addMonths'  for calculating the next-interval date for sending notifications.
'msg91' third-party API(needs a paid plan on Firebase to work) to send appropriate 
 message to appropriate customer.
 */

const functions = require('firebase-functions');  //straightheirarchy db
 const admin = require('firebase-admin');

 const add = require('./addMonths.js');


admin.initializeApp();

const ref = admin.database().ref();

exports.sendnotif =  functions.https.onRequest((req,res) => {
  const array_prod = [];
  const names = [];
  const customerKeys =[];


   var j = 0;

   var yyyy = new Date().getFullYear();
   var mm = new Date().getMonth() + 1;
   var dd= new Date().getDate();

   if(dd <= 9)
   {
     dd = '0'+ dd;
   }
   if(mm <= 9)
   {
     mm = '0' + mm;
   }

   var today = yyyy+'-'+mm+'-'+dd;
  ref.child("customers").once("value").then(snap  => {

    const childSnap = snap.val();                 //childSnap has all customers
    //res.send(Object.keys(childSnap));
      Object.keys(childSnap).forEach(key/*customer*/ => {
      array_prod.push(childSnap[key].products);  //array contains all customers' products
      names.push(childSnap[key].name);       //names contains all customers' names
      customerKeys.push(key)                 //customerKeys contains all customers' UIDs
      });
      return array_prod;
    }).then(array_prod => {
      const phones = [];

      for( j=0 ; j < array_prod.length; j++)
    {
        Object.keys(array_prod[j]).forEach(productkey/*productkey*/ => {
          var dateOfPurchase =(array_prod[j])[productkey].date_of_purchase;
          var service_period = (array_prod[j])[productkey].interval;
          var productname = (array_prod[j])[productkey].pname;

          var enddate = (array_prod[j])[productkey].end_date;
          if(enddate)
          {
            if(today === enddate)
            {

              var phonenum = (array_prod[j])[productkey].phone;
              phones.push(phonenum);
              var customername = names[j];
              console.log(`Sending to: ${customername} , ${phonenum} for bye bye`);

              const message1 = `Hi ${customername}! Your journey with us for ${productname} has ended with us.Thank you for your presence with us.`;
                 ref.child("tokens").child(customerKeys[j]).once("value").then(ssnap => {

                 const token = ssnap.val();
                
                 console.log("Construction the notification message.");
                 const payload = {
                        data: {
                          data_type:"direct_message",
                          title: "New message by Service App",
                          message: message1
                        }
                      };
                             
                        admin.messaging().sendToDevice(token, payload)
                        .then(response => {
                          console.log("Response:"+ JSON.stringify(response));
                          })
                          .catch(error  => {
                          console.log("Error " + error);
                          });                 
                });  



                var enddateRef = ref.child(`customers/${customerKeys[j]}/products/${productkey}`);
                enddateRef.remove()
                .then(response => {
                   console.log(response  + " Succesful removal.");
                })
                .catch(err => {
                  console.log(err + " Error caught when deleting");
                });
            }
          }

          var calculated = add.addMonths(new Date(dateOfPurchase),service_period);
              
              if(today === calculated)
              {
                var phonenum = (array_prod[j])[productkey].phone;
                phones.push(phonenum);
                var customername = names[j];
                console.log(`Sending to: ${customername} , ${phonenum}`);
                const message = `Hi ${customername}!\n\nYour service for ${productname} is due.\nVisit the stores within 2 days to avail the service.`;


                ref.child("tokens").child(customerKeys[j]).once("value").then(ssnap => {

                const token = ssnap.val();
                
                 console.log("Construction the notification message.");
                 const payload = {
                        data: {
                          data_type:"direct_message",
                          title: "Reminder by Service App",
                          message: message
                        }
                      };

                  admin.messaging().sendToDevice(token, payload)
                  .then(response => {
                   console.log("Response:"+ JSON.stringify(response));
                   })
                   .catch(error  => {
                   console.log("Error " + error);
                   });     
             });  

 
                // ref.child("sellers").once("value").then(snapshot => {
                //   snapshot.forEach(childshot => {
                //     console.log(childshot);
                //     var bool = childshot.child(customerKeys[j]).exists();
                //     console.log(bool);
                    
                //     if(bool)
                //     {
                //       var sellerid = childshot.key;
                //       console.log(sellerid);
                //       ref.child("Seller_tokens").child(sellerid).once("value").then(sellersnap => {
                //       token = sellersnap.val();
                //       const seller_message = `Hi! Your customer ${customername} is due for service in 2 days for product:${productname}.`;

                //                     console.log("Construction the notification message.");
                //                     payload = {
                //                     data: {
                //                       data_type:"direct_message",
                //                       title: "New message by Service App",
                //                       message: seller_message
                //                     }
                //                   };
     
                //                     admin.messaging().sendToDevice(token,  payload)
                //                     .then(response => {
                //                       console.log("Response:"+ JSON.stringify(response));
                //                       })
                //                       .catch(error  => {
                //                       console.log("Error " + error);
                //                       });    
                //            });
                //      }   //if that seller is the customers service provider
                //   });
                // }).catch(err => {
                //    res.send(err);
                // });
                
                var dateRef = ref.child(`customers/${customerKeys[j]}/products/${productkey}`);
                dateRef.update({
                  "date_of_purchase":`${today}`
                 });     //update new date
              
                
            }  //if(today==calculated)
      
        });
  
    } 
    return phones;
}).then(phones => {

   res.send(phones);
    
}).catch(err => res.send(err));
});


exports.sendNotification = functions.database.ref('/sellers/{sellerId}/{messageId}').onWrite((change,context) => {
  
  
	//get the userId of the person receiving the notification because we need to get their token
	const sellerId = context.params.sellerId;
	console.log("sellerId: ", sellerId);
	const ref = admin.database().ref();
	//get the user id of the person who sent the message
	// const senderId = event.data.child('user_id').val();
	// console.log("senderId: ", senderId);
	
	//get the message id. We'll be sending this in the payload
	const messageId = context.params.messageId;
  console.log("messageId: ", messageId);
  
  //get the message
	const message = change.after.val();
	console.log("message: ", message);
	
	//query the users node and get the name of the user who sent the message
  ref.child("sellers").child(sellerId).once("value").then(snapshot =>
  {
    var carray = [];
    var sample = ["name","email","isSeller","phone",`${messageId}`];
    Object.keys(snapshot.val()).forEach(key => {
      console.log(key);
      if(!(sample.includes(key)))
      {carray.push(key);}
    });
    return carray;
  }).then(carray => {
    var i = 0;
    for(i = 0; i < carray.length ;i++)
    {
      ref.child("tokens").child(carray[i]).once("value").then(snap => {
        var token  = snap.val();

        console.log("Construction the notification message.");
        const payload = {
          data: {
            data_type: "direct_message",
            title: "New Message from ",   ///////////////////
            message: message,
            message_id: messageId,
          }
        };
        if(token)
        {
        return admin.messaging().sendToDevice(token, payload)
              .then(function(response) {
                console.log("Successfully sent message:", response);
                })
                .catch(function(error) {
                console.log("Error sending message:", error);
                });
        }

      });
    }
       ref.child('/sellers/{sellerId}/{messageId}').remove()
        .then(response => {
          console.log(response  + " Succesful removal.");
      })
      .catch(err => {
        console.log(err + " Error caught when deleting");
      });
  });
			
			//we have everything we need
			//Build the message payload and send the message
		
});















