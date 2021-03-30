using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models.Database
{
    public class Follow
    {
        public string FolloweeID { get; set; }
        public UserModel Followee { get; set; }

        public string FollowerID { get; set; }
        public UserModel Follower { get; set; }

        [Required]
        public DateTime RequestTime { get; set; }

        [Required]
        public bool Accepted { get; set; }
    }
}
