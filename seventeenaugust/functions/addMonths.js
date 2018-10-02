

exports.addMonths = (date, months) => {
  var d = date.getDate();
  date.setMonth(date.getMonth() + +months);
  if (date.getDate() != d) {
    date.setDate(0);
  }
  

  d = date.getDate();
  var m = date.getMonth() + 1;
  var y = date.getFullYear(); 

  if(date.getDate() <= 9)
  {
    d ='0'+ d;
  }
  if((date.getMonth()+1) <= 9)
  {
    m ='0'+ m;
  }
  var today = y+'-'+m+'-'+d;
  return today;
}

