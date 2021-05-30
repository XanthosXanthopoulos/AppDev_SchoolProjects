using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Database
{
    public class Comment
    {
        [Key]
        public string CommentID { get; set; }

        public User User { get; set; }

        public string Content { get; set; }
    }
}
