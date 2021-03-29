﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Models.Enums;

namespace WebServer.Models.Api.Response
{
    public class ProfileInfoResponseModel
    {
        public string Username { get; set; }

        public string Email { get; set; }

        public string Name { get; set; }

        public string Surname { get; set; }

        public DateTime Birthday { get; set; }

        public Country Country { get; set; }

        public string Description { get; set; }

        public AccountType AccountType { get; set; }
    }
}