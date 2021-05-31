using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models.Database
{
    public class Post
    {
        [Key]
        public int PostID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public DateTime Date { get; set; }

        public ICollection<Image> Images { get; set; } 

        public ICollection<PostActivity> PostActivities { get; set; }

        public ICollection<Like> Likes { get; set; }

        public ICollection<Comment> Comments { get; set; }

        [ForeignKey("User")]
        public string UserID { get; set; }

        public User User { get; set; }
    }
}
