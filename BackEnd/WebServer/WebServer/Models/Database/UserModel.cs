using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using WebServer.Models.Enums;

namespace WebServer.Models.Database
{
    public class UserModel
    {
        [Key]
        public string UserID { get; set; }

        public string Name { get; set; }

        public string Surname { get; set; }

        public DateTime Birthday { get; set; }

        public Country Country { get; set; }

        public string Description { get; set; }

        public AccountType AccountType { get; set; }

        public Image ProfileImage { get; set; }

        public ICollection<Post> Posts { get; set; }
    }
}
