using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Models.Database;
using WebServer.Models.Enums;

namespace WebServer.Models.Api.Response
{
    public class PostResponseModel
    {
        public int PostID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public string ProfileImageID { get; set; }

        public string Username { get; set; }

        public int Likes { get; set; }

        public DateTime Date { get; set; }

        public ICollection<string> Images { get; set; }

        public ICollection<ActivityResponse> Activities { get; set; }

        public ICollection<CommentResponse> Comments { get; set; }
    }
}
