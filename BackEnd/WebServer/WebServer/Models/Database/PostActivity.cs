using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Database
{
    public class PostActivity
    {
        public int PostID { get; set; }

        public Post Post { get; set; }

        public string ActivityID { get; set; }

        public Activity Activity { get; set; }
    }
}
