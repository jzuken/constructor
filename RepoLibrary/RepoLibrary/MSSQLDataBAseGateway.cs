using System;
using System.Data;
using System.Data.SqlClient;

namespace RepoLibrary
{
    class MSSQLDatabaseGateway : IDatabaseGateway
    {
        public MSSQLDatabaseGateway(string connectionString)
        {
            this.connectionString = connectionString;
        }

        public Project GetProject(string url)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlDataReader myReader = null;
            SqlCommand myCommand = new SqlCommand("select * from shops where shopUrl=@url", connection);
            myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = url;
            myReader = myCommand.ExecuteReader();
            while (myReader.Read())
            {
                Project project = new Project();
                project.Settings = myReader["shopSettings"].ToString();
                project.Url = myReader["shopUrl"].ToString();
                project.ExpirationDate = myReader["shopExpirationDate"].ToString();
                project.apiUrl = myReader["shopApiUrl"].ToString();
                project.keyHash = myReader["shopKeyHash"].ToString();
                project.firstExpiredLogin = myReader["shopFirstExpiredDate"].ToString();
                project.trialEndDate = myReader["trialEndDate"].ToString();
                project.subscribtionStartDate = myReader["subscribtionStartDate"].ToString();
                connection.Close();
                return project;
            }
            connection.Close();
            return null;
        }

        public string SaveProject(Project data)
        {
            Project project = GetProject(data.Url);
            if (project == null)
            {
                SqlConnection connection = new SqlConnection(this.connectionString);
                connection.Open();
                SqlCommand myCommand = new SqlCommand("INSERT INTO shops (shopUrl, shopSettings, shopExpirationDate, shopApiUrl, shopKeyHash, shopFirstExpiredDate, trialEndDate, subscribtionStartDate) " +
                    "Values (@url,@settings,@expiration,@apiUrl,@keyHash,@shopFirstExpired,@trialEndDate,@subscribtionStartDate)", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
                myCommand.Parameters.Add("@apiUrl", SqlDbType.VarChar).Value = data.apiUrl;
                myCommand.Parameters.Add("@keyHash", SqlDbType.VarChar).Value = data.keyHash;
                myCommand.Parameters.Add("@shopFirstExpired", SqlDbType.VarChar).Value = data.firstExpiredLogin;
                myCommand.Parameters.Add("@trialEndDate", SqlDbType.VarChar).Value = data.trialEndDate;
                myCommand.Parameters.Add("@subscribtionStartDate", SqlDbType.VarChar).Value = data.subscribtionStartDate;
                int result = myCommand.ExecuteNonQuery();
                connection.Close();
                if (result > 0)
                {
                    return "ok";
                }
                else
                {
                    return "Failed to add project";
                }
            }
            else
            {
                SqlConnection connection = new SqlConnection(this.connectionString);
                connection.Open();
                SqlCommand myCommand = new SqlCommand("UPDATE shops SET shopSettings=@settings, shopExpirationDate=@expiration, shopApiUrl=@apiUrl, shopKeyHash=@keyHash, shopFirstExpiredDate=@shopFirstExpired, trialEndDate=@trialEndDate, subscribtionStartDate=@subscribtionStartDate WHERE shopUrl=@url", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
                myCommand.Parameters.Add("@apiUrl", SqlDbType.VarChar).Value = data.apiUrl;
                myCommand.Parameters.Add("@keyHash", SqlDbType.VarChar).Value = data.keyHash;
                myCommand.Parameters.Add("@shopFirstExpired", SqlDbType.VarChar).Value = data.firstExpiredLogin;
                myCommand.Parameters.Add("@trialEndDate", SqlDbType.VarChar).Value = data.trialEndDate;
                myCommand.Parameters.Add("@subscribtionStartDate", SqlDbType.VarChar).Value = data.subscribtionStartDate;
                int result = myCommand.ExecuteNonQuery();
                connection.Close();
                if (result > 0)
                {
                    return "ok";
                }
                else
                {
                    return "Failed to add project";
                }
            }
        }
        private string connectionString;
    }
}