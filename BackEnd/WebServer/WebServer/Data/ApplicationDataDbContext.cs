using Microsoft.EntityFrameworkCore;
using System;
using WebServer.Models.Database;

namespace WebServer.Data
{
    public class ApplicationDataDbContext : DbContext
    {
        #region Public Dababase Sets

        public DbSet<UserModel> Users { get; set; }

        public DbSet<ImageModel> Images { get; set; }

        public DbSet<Follow> Follows { get; set; }

        public DbSet<Activity> Activities { get; set; }

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
            modelBuilder.Entity<Follow>().HasOne(f => f.Follower).WithMany().HasForeignKey(f => f.FollowerID);
            modelBuilder.Entity<Follow>().HasOne(f => f.Followee).WithMany().HasForeignKey(f => f.FolloweeID).OnDelete(DeleteBehavior.NoAction);

            modelBuilder.Entity<Activity>().Property(a => a.Tags).HasConversion(a => string.Join(",", a), a => a.Split(',', StringSplitOptions.RemoveEmptyEntries));
            modelBuilder.Entity<Activity>().Property(e => e.ID).ValueGeneratedOnAdd();
        }

        #endregion
    }
}
