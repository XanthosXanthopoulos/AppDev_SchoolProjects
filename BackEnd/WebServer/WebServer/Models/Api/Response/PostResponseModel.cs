using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Response
{
    public class PostResponseModel
    {
        public string ContentType { get; set; }

        public string Username { get; set; }

        public string ProfileImageID { get; set; }

        public string ThumbnailImageID { get; set; }

        public string Description { get; set; }
    }
}
