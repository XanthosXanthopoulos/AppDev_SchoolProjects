using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Response
{
    public class CommentResponse
    {
        public string Username { get; set; }

        public string ProfileImageID { get; set; }

        public string Content { get; set; }
    }
}
