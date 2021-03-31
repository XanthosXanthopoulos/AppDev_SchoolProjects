using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Request
{
    public class FollowResponseModel
    {
        public string FollowerID { get; set; }

        public bool Accepted { get; set; }
    }
}
