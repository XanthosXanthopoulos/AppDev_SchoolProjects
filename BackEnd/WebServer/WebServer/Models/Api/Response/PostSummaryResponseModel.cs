using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Response
{
    public class PostSummaryResponseModel
    {
        public int PostID { get; set; }

        public string Username { get; set; }

        public string Title { get; set; }

        public DateTime Date { get; set; }

        public string CountrySummary { get; set; }

        public string ProfileImageID { get; set; }

        public string ThumbnailImageID { get; set; }

        public int Likes { get; set; }

        public bool Liked { get; set; }
    }
}
