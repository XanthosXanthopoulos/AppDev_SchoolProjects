using Microsoft.EntityFrameworkCore;
using System;
using WebServer.Models.Database;

namespace WebServer.Data
{
    public class ApplicationDataDbContext : DbContext
    {
        #region Public Dababase Sets

        public DbSet<User> Users { get; set; }

        public DbSet<Image> Images { get; set; }

        public DbSet<Follow> Follows { get; set; }

        public DbSet<Activity> Activities { get; set; }

        public DbSet<Post> Posts { get; set; }

        public DbSet<Like> Likes { get; set; }

        public DbSet<Comment> Comments { get; set; }

        public DbSet<PostActivity> PostActivity { get; set; }

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

            modelBuilder.Entity<Activity>().Property(e => e.ActivityID).ValueGeneratedOnAdd();

            modelBuilder.Entity<Post>().Property(e => e.PostID).ValueGeneratedOnAdd();
            modelBuilder.Entity<Like>().Property(e => e.LikeID).ValueGeneratedOnAdd();
            modelBuilder.Entity<Comment>().Property(e => e.CommentID).ValueGeneratedOnAdd();

            modelBuilder.Entity<Post>().HasMany(p => p.Images).WithOne().IsRequired(false).OnDelete(DeleteBehavior.SetNull);

            modelBuilder.Entity<PostActivity>().HasKey(sc => new { sc.PostID, sc.ActivityID });

            modelBuilder.Entity<PostActivity>()
                .HasOne(sc => sc.Post)
                .WithMany(s => s.PostActivities)
                .HasForeignKey(sc => sc.PostID);


            modelBuilder.Entity<PostActivity>()
                .HasOne(sc => sc.Activity)
                .WithMany(s => s.PostActivities)
                .HasForeignKey(sc => sc.ActivityID);
        }

        #endregion
    }
}
