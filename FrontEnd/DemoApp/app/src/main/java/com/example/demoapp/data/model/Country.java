package com.example.demoapp.data.model;

public enum Country
{
    ANY("Select country", 0),
    AF("Afghanistan", 1),
    AX("Åland Islands", 2),
    AL("Albania", 3),
    DZ("Algeria", 4),
    AS("American Samoa", 5),
    AD("Andorra", 6),
    AO("Angola", 7);
//        Anguilla")] AI = 8,
//        Antarctica")] AQ = 9,
//        Antigua and Barbuda")] AG = 10,
//        Argentina")] AR = 11,
//        Armenia")] AM = 12,
//        Aruba")] AW = 13,
//        Australia")] AU = 14,
//        Austria")] AT = 15,
//        Azerbaijan")] AZ = 16,
//        Bahamas")] BS = 17,
//        Bahrain")] BH = 18,
//        Bangladesh")] BD = 19,
//        Barbados")] BB = 20,
//        Belarus")] BY = 21,
//        Belgium")] BE = 22,
//        Belize")] BZ = 23,
//        Benin")] BJ = 24,
//        Bermuda")] BM = 25,
//        Bhutan")] BT = 26,
//        Bolivia (Plurinational State of)")] BO = 27,
//        Bonaire, Sint Eustatius and Saba")] BQ = 28,
//        Bosnia and Herzegovina")] BA = 29,
//        Botswana")] BW = 30,
//        Bouvet Island")] BV = 31,
//        Brazil")] BR = 32,
//        British Indian Ocean Territory")] IO = 33,
//        Brunei Darussalam")] BN = 34,
//        Bulgaria")] BG = 35,
//        Burkina Faso")] BF = 36,
//        Burundi")] BI = 37,
//        Cabo Verde")] CV = 38,
//        Cambodia")] KH = 39,
//        Cameroon")] CM = 40,
//        Canada")] CA = 41,
//        Cayman Islands")] KY = 42,
//        Central African Republic")] CF = 43,
//        Chad")] TD = 44,
//        Chile")] CL = 45,
//        China")] CN = 46,
//        Christmas Island")] CX = 47,
//        Cocos (Keeling) Islands")] CC = 48,
//        Colombia")] CO = 49,
//        Comoros")] KM = 50,
//        Congo")] CG = 51,
//        Congo (Democratic Republic of the)")] CD = 52,
//        Cook Islands")] CK = 53,
//        Costa Rica")] CR = 54,
//        Côte d'Ivoire")] CI = 55,
//        Croatia")] HR = 56,
//        Cuba")] CU = 57,
//        Curaçao")] CW = 58,
//        Cyprus")] CY = 59,
//        Czechia")] CZ = 60,
//        Denmark")] DK = 61,
//        Djibouti")] DJ = 62,
//        Dominica")] DM = 63,
//        Dominican Republic")] DO = 64,
//        Ecuador")] EC = 65,
//        Egypt")] EG = 66,
//        El Salvador")] SV = 67,
//        Equatorial Guinea")] GQ = 68,
//        Eritrea")] ER = 69,
//        Estonia")] EE = 70,
//        Ethiopia")] ET = 71,
//        Falkland Islands (Malvinas)")] FK = 72,
//        Faroe Islands")] FO = 73,
//        Fiji")] FJ = 74,
//        Finland")] FI = 75,
//        France")] FR = 76,
//        French Guiana")] GF = 77,
//        French Polynesia")] PF = 78,
//        French Southern Territories")] TF = 79,
//        Gabon")] GA = 80,
//        Gambia")] GM = 81,
//        Georgia")] GE = 82,
//        Germany")] DE = 83,
//        Ghana")] GH = 84,
//        Gibraltar")] GI = 85,
//        Greece")] GR = 86,
//        Greenland")] GL = 87,
//        Grenada")] GD = 88,
//        Guadeloupe")] GP = 89,
//        Guam")] GU = 90,
//        Guatemala")] GT = 91,
//        Guernsey")] GG = 92,
//        Guinea")] GN = 93,
//        Guinea-Bissau")] GW = 94,
//        Guyana")] GY = 95,
//        Haiti")] HT = 96,
//        Heard Island and McDonald Islands")] HM = 97,
//        Holy See")] VA = 98,
//        Honduras")] HN = 99,
//        Hong Kong")] HK = 100,
//        Hungary")] HU = 101,
//        Iceland")] IS = 102,
//        India")] IN = 103,
//        Indonesia")] ID = 104,
//        Iran (Islamic Republic of)")] IR = 105,
//        Iraq")] IQ = 106,
//        Ireland")] IE = 107,
//        Isle of Man")] IM = 108,
//        Israel")] IL = 109,
//        Italy")] IT = 110,
//        Jamaica")] JM = 111,
//        Japan")] JP = 112,
//        Jersey")] JE = 113,
//        Jordan")] JO = 114,
//        Kazakhstan")] KZ = 115,
//        Kenya")] KE = 116,
//        Kiribati")] KI = 117,
//        Korea (Democratic People's Republic of)")] KP = 118,
//        Korea (Republic of)")] KR = 119,
//        Kuwait")] KW = 120,
//        Kyrgyzstan")] KG = 121,
//        Lao People's Democratic Republic")] LA = 122,
//        Latvia")] LV = 123,
//        Lebanon")] LB = 124,
//        Lesotho")] LS = 125,
//        Liberia")] LR = 126,
//        Libya")] LY = 127,
//        Liechtenstein")] LI = 128,
//        Lithuania")] LT = 129,
//        Luxembourg")] LU = 130,
//        Macao")] MO = 131,
//        Macedonia (the former Yugoslav Republic of)")] MK = 132,
//        Madagascar")] MG = 133,
//        Malawi")] MW = 134,
//        Malaysia")] MY = 135,
//        Maldives")] MV = 136,
//        Mali")] ML = 137,
//        Malta")] MT = 138,
//        Marshall Islands")] MH = 139,
//        Martinique")] MQ = 140,
//        Mauritania")] MR = 141,
//        Mauritius")] MU = 142,
//        Mayotte")] YT = 143,
//        Mexico")] MX = 144,
//        Micronesia (Federated States of)")] FM = 145,
//        Moldova (Republic of)")] MD = 146,
//        Monaco")] MC = 147,
//        Mongolia")] MN = 148,
//        Montenegro")] ME = 149,
//        Montserrat")] MS = 150,
//        Morocco")] MA = 151,
//        Mozambique")] MZ = 152,
//        Myanmar")] MM = 153,
//        Namibia")] NA = 154,
//        Nauru")] NR = 155,
//        Nepal")] NP = 156,
//        Netherlands")] NL = 157,
//        New Caledonia")] NC = 158,
//        New Zealand")] NZ = 159,
//        Nicaragua")] NI = 160,
//        Niger")] NE = 161,
//        Nigeria")] NG = 162,
//        Niue")] NU = 163,
//        Norfolk Island")] NF = 164,
//        Northern Mariana Islands")] MP = 165,
//        Norway")] NO = 166,
//        Oman")] OM = 167,
//        Pakistan")] PK = 168,
//        Palau")] PW = 169,
//        Palestine, State of")] PS = 170,
//        Panama")] PA = 171,
//        Papua New Guinea")] PG = 172,
//        Paraguay")] PY = 173,
//        Peru")] PE = 174,
//        Philippines")] PH = 175,
//        Pitcairn")] PN = 176,
//        Poland")] PL = 177,
//        Portugal")] PT = 178,
//        Puerto Rico")] PR = 179,
//        Qatar")] QA = 180,
//        Réunion")] RE = 181,
//        Romania")] RO = 182,
//        Russian Federation")] RU = 183,
//        Rwanda")] RW = 184,
//        Saint Barthélemy")] BL = 185,
//        Saint Helena, Ascension and Tristan da Cunha")] SH = 186,
//        Saint Kitts and Nevis")] KN = 187,
//        Saint Lucia")] LC = 188,
//        Saint Martin (French part)")] MF = 189,
//        Saint Pierre and Miquelon")] PM = 190,
//        Saint Vincent and the Grenadines")] VC = 191,
//        Samoa")] WS = 192,
//        San Marino")] SM = 193,
//        Sao Tome and Principe")] ST = 194,
//        Saudi Arabia")] SA = 195,
//        Senegal")] SN = 196,
//        Serbia")] RS = 197,
//        Seychelles")] SC = 198,
//        Sierra Leone")] SL = 199,
//        Singapore")] SG = 200,
//        Sint Maarten (Dutch part)")] SX = 201,
//        Slovakia")] SK = 202,
//        Slovenia")] SI = 203,
//        Solomon Islands")] SB = 204,
//        Somalia")] SO = 205,
//        South Africa")] ZA = 206,
//        South Georgia and the South Sandwich Islands")] GS = 207,
//        South Sudan")] SS = 208,
//        Spain")] ES = 209,
//        Sri Lanka")] LK = 210,
//        Sudan")] SD = 211,
//        Suriname")] SR = 212,
//        Svalbard and Jan Mayen")] SJ = 213,
//        Swaziland")] SZ = 214,
//        Sweden")] SE = 215,
//        Switzerland")] CH = 216,
//        Syrian Arab Republic")] SY = 217,
//        Taiwan, Province of China[a]")] TW = 218,
//        Tajikistan")] TJ = 219,
//        Tanzania, United Republic of")] TZ = 220,
//        Thailand")] TH = 221,
//        Timor-Leste")] TL = 222,
//        Togo")] TG = 223,
//        Tokelau")] TK = 224,
//        Tonga")] TO = 225,
//        Trinidad and Tobago")] TT = 226,
//        Tunisia")] TN = 227,
//        Turkey")] TR = 228,
//        Turkmenistan")] TM = 229,
//        Turks and Caicos Islands")] TC = 230,
//        Tuvalu")] TV = 231,
//        Uganda")] UG = 232,
//        Ukraine")] UA = 233,
//        United Arab Emirates")] AE = 234,
//        United Kingdom of Great Britain and Northern Ireland")] GB = 235,
//        United States of America")] US = 236,
//        United States Minor Outlying Islands")] UM = 237,
//        Uruguay")] UY = 238,
//        Uzbekistan")] UZ = 239,
//        Vanuatu")] VU = 240,
//        Venezuela (Bolivarian Republic of)")] VE = 241,
//        Viet Nam")] VN = 242,
//        Virgin Islands (British)")] VG = 243,
//        Virgin Islands (U.S.)")] VI = 244,
//        Wallis and Futuna")] WF = 245,
//        Western Sahara")] EH = 246,
//        Yemen")] YE = 247,
//        Zambia")] ZM = 248,
//        Zimbabwe")] ZW = 249,

    public final String label;
    public final int code;

    Country(String label, int code)
    {
        this.label = label;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
