using System.ComponentModel.DataAnnotations;

namespace WebServer.Models.Database
{
    public class Like
    {
        [Key]
        public string LikeID { get; set; }

        public User User { get; set; }
    }
}
