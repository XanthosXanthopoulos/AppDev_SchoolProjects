using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WebServer.Models.Enums;

namespace WebServer.Models.Database
{
    public class User
    {
        [Key]
        public string UserID { get; set; }

        public string Name { get; set; }

        public string Surname { get; set; }

        public DateTime Birthday { get; set; }

        public Country Country { get; set; }

        public string Description { get; set; }

        public AccountType AccountType { get; set; }

        [ForeignKey("Image")]
        public string ImageID { get; set; }

        public Image Image { get; set; }

        public ICollection<Post> Posts { get; set; }
    }
}
