namespace WebServer.Models.Api.Response
{
    public class ApiResponse<T>
    {
        #region Public Properties

        /// <summary>
        /// Indicates if the API call is successful
        /// </summary>
        public bool Successful => ErrorMessage == null;

        /// <summary>
        /// The error mesaage for a failed API call
        /// </summary>
        public string ErrorMessage { get; set; }

        /// <summary>
        /// The API response object
        /// </summary>
        public T Response { get; set; }

        #endregion
    }
}
