package com.ssafy.family.util

import java.util.*

private const val TAG = "InputValidUtil_strait"
object InputValidUtil {
    val nameRegex = "^[가-힣a-zA-Z]{2,20}$".toRegex()
    val emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$".toRegex()
    //영문, 숫자
    val passRegex1 = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,14}$".toRegex()
    //영문, 특문
    val passRegex2 = "^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,14}$".toRegex()
    //특문, 숫자
    val passRegex3 = "^(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{8,14}$".toRegex()
    val passRegexes = listOf(passRegex1, passRegex2, passRegex3)

    val birthDayRegex = "([0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1]))".toRegex()
    val phoneRegex = "^\\d{3}\\d{3,4}\\d{4}$".toRegex()

    fun isValidEmail(email: String): Boolean {
        val result = email.matches(emailRegex)
        return result
    }

    fun isValidName(name: String): Boolean {
        val result = name.matches(nameRegex)
        return result
    }

    fun isValidPassword(password: String): Boolean {
        for(i in passRegexes) {
            if(password.matches(i))
                return true
        }
        return false
    }


    // todo: 날짜 계산
    fun isValidBirthDay(birthDay: String): Boolean {
        if(birthDay.matches(birthDayRegex)) {
            val year = birthDay.substring(0, 2).toInt()
            val month = birthDay.substring(2, 4).toInt()
            val day = birthDay.substring(4, 6).toInt()

            var convertedYear = if(year >= 0 && year <= Calendar.getInstance().get(Calendar.YEAR) % 100) {
                2000 + year
            } else {
                1900 + year
            }

            return isValidDay(convertedYear, month, day)
        }
        return false
    }

    fun isValidDay(year: Int, month: Int, day: Int): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        return if (1900 > year || year > currentYear) {
            false
        } else if (month < 1 || month > 12) {
            false
        } else if (day < 1 || day > 31) {
            false
        } else if ((month==4 || month==6 || month==9 || month==11) && day==31) {
            false
        } else if (month == 2) {
            val isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
            !(day > 29 || (day == 29 && !isLeap))
        } else {
            true
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(phoneRegex)
    }
}