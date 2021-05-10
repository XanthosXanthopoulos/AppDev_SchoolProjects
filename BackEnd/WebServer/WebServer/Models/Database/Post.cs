using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WebServer.Models.Enums;

namespace WebServer.Models.Database
{
    public class Post
    {
        [Key]
        public int PostID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public Country Country { get; set; }

        public ICollection<Image> Images { get; set; } 

        public ICollection<Activity> Activities { get; set; }

        [ForeignKey("User")]
        public string UserID { get; set; }

        public UserModel User { get; set; }
    }
}
