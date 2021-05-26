using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Request
{
    public class FollowResponseModel
    {
        public string UserID { get; set; }

        public string Username { get; set; }

        public string ProfileImageID { get; set; }

        public string Status { get; set; }
    }
}
