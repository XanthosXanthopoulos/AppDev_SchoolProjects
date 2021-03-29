using Microsoft.EntityFrameworkCore;
using WebServer.Models.Database;

namespace WebServer.Data
{
    public class ApplicationDataDbContext : DbContext
    {
        #region Public Dababase Sets

        public DbSet<UserModel> Users { get; set; }

        public DbSet<ImageModel> Images { get; set; }

        public DbSet<Follow> Follows { get; set; }

        #endregion

        #region Constructor

        public ApplicationDataDbContext(DbContextOptions<ApplicationDataDbContext> options) : base(options)
        {

        }

        #endregion

        #region Overrides

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Follow>().HasKey(f => new { f.FolloweeID, f.FollowerID });
        }

        #endregion
    }
}
