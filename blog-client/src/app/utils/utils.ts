export function getDate(date: any): string {
  const time = new Date(date);
    
  let am = false;
  let hours = time.getHours();
    
  if (hours >= 12 && hours <= 23) {
    am = false;
  } else {
    am = true;
  }

  if(hours > 12) {
    hours = hours % 12;
  }
  
  return `${getMonth(time.getMonth())} ${time.getDate()}, ${time.getFullYear()} ${hours}:${time.getMinutes()}${am? 'am':'pm'}`;
}

function getMonth(month: number) {
  const months: Record<number, string> = {
    1: 'Jan',
    2: 'Feb',
    3: 'Mar',
    4: 'Apr',
    5: 'May',
    6: 'Jun',
    7: 'Jul',
    8: 'Aug',
    9: 'Sep',
    10: 'Oct',
    11: 'Nov',
    12: 'Dec'
  };

  return months[month];
}