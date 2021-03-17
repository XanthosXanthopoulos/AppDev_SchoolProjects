using Microsoft.EntityFrameworkCore;
using WebServer.Models.Database;

namespace WebServer.Data
{
    public class ApplicationDataDbContext : DbContext
    {
        #region Public Dababase Sets

        public DbSet<UserModel> Users { get; set; }

        public DbSet<ImageModel> Images { get; set; }

        #endregion

        #region Constructor

        public ApplicationDataDbContext(DbContextOptions<ApplicationDataDbContext> options) : base(options)
        {

        }

        #endregion
    }
}
