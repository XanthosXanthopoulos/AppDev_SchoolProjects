using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Models.Api.Response;

namespace WebServer.Utilities
{
    public class ActivityEqualityComparator : IEqualityComparer<ActivityResponse>
    {
        public bool Equals([AllowNull] ActivityResponse x, [AllowNull] ActivityResponse y)
        {
            return x.ID.Equals(y.ID);
        }

        public int GetHashCode([DisallowNull] ActivityResponse obj)
        {
            return obj.ID.GetHashCode();
        }
    }

    public class PostEqualityComparator : IEqualityComparer<PostSummaryResponseModel>
    {
        public bool Equals([AllowNull] PostSummaryResponseModel x, [AllowNull] PostSummaryResponseModel y)
        {
            return x.PostID == y.PostID;
        }

        public int GetHashCode([DisallowNull] PostSummaryResponseModel obj)
        {
            return obj.PostID;
        }
    }
}
