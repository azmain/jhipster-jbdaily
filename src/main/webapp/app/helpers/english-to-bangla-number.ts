export class EnglishToBanglaNumber {
  static convertToBanglaNumber(number: number | null | undefined, komma = false): string | null | undefined {
    const englishNumbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
    const banglaNumbers = ['০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'];

    if (number === null || number === undefined) {
      return number;
    }

    // const numberStr = number.toString();
    let numberStr = `${number.toLocaleString('bn-BD', { useGrouping: komma })}`;

    let banglaNumber = '';

    // for (var x in banglaNumber) {
    //     str = str.replace(new RegExp(x, 'g'), banglaNumber[x])
    // }

    for (let i = 0; i < numberStr.length; i++) {
      const digit = numberStr[i];
      const index = englishNumbers.indexOf(digit);
      if (index !== -1) {
        banglaNumber += banglaNumbers[index];
      } else {
        banglaNumber += digit; // If not a digit, keep it as is.
      }
    }

    return banglaNumber;
  }

  static convertToBanglaString(englishString: string): string {
    const searchArray = [
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '/',
      '-',
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December',
      'am',
      'AM',
      'pm',
      'PM',
    ];
    const replaceArray = [
      '১',
      '২',
      '৩',
      '৪',
      '৫',
      '৬',
      '৭',
      '৮',
      '৯',
      '০',
      '/',
      '-',
      'জানুয়ারি',
      'ফেব্রুয়ারি',
      'মার্চ',
      'এপ্রিল',
      'মে',
      'জুন',
      'জুলাই',
      'আগস্ট',
      'সেপ্টেম্বর',
      'অক্টোবর',
      'নভেম্বর',
      'ডিসেম্বর',
      'সকাল',
      'সকাল',
      'বিকাল',
      'বিকাল',
    ];

    let bnText = englishString;

    for (let i = 0; i < searchArray.length; i++) {
      const regex = new RegExp(searchArray[i], 'g');
      bnText = bnText.replace(regex, replaceArray[i]);
    }

    return bnText;
  }

  static formatDateInfo(dateString: string): [number, string, number] {
    const months = [
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December',
    ];

    const date = new Date(dateString);
    const day = date.getDate(); // Get the day of the month (1-31)
    const monthName = months[date.getMonth()]; // Get the full month name
    const year = date.getFullYear(); // Get the year (e.g., 2023)

    return [day, monthName, year];
  }

  static formatDateInfoBangla(dateString: string): [string, string, string] {
    const months = [
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December',
    ];

    const date = new Date(dateString);
    const day = date.getDate(); // Get the day of the month (1-31)
    const monthName = months[date.getMonth()]; // Get the full month name
    const year = date.getFullYear(); // Get the year (e.g., 2023)

    return [
      EnglishToBanglaNumber.convertToBanglaString(`${day}`),
      EnglishToBanglaNumber.convertToBanglaString(`${monthName}`),
      EnglishToBanglaNumber.convertToBanglaString(`${year}`),
    ];
  }
}
