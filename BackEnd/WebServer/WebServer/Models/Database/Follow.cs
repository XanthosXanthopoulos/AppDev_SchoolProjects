using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models.Database
{
    public class Follow
    {
        public string FolloweeID { get; set; }
        public User Followee { get; set; }

        public string FollowerID { get; set; }
        public User Follower { get; set; }

        [Required]
        public DateTime RequestTime { get; set; }

        [Required]
        public bool Accepted { get; set; }
    }
}
