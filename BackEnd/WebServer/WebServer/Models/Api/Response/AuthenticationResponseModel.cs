using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Response
{
    public class AuthenticationResponseModel
    {
        public string Username { get; set; }

        public string JWToken { get; set; }

        public string ProfileImageID { get; set; }
    }
}
