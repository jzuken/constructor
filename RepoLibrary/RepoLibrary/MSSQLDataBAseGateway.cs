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
                Console.WriteLine(myReader["shopUrl"].ToString());
                Console.WriteLine(myReader["shopSettings"].ToString());
                Console.WriteLine((myReader["shopExpirationDate"] != null).ToString());
                Project project = new Project();
                project.Settings = myReader["shopSettings"].ToString();
                project.Url = myReader["shopUrl"].ToString();
                project.ExpirationDate = myReader["shopExpirationDate"].ToString();
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
                SqlCommand myCommand = new SqlCommand("INSERT INTO shops (shopUrl, shopSettings, shopExpirationDate) " +
                    "Values (@url,@settings,@expiration)", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
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
                SqlCommand myCommand = new SqlCommand("UPDATE shops SET shopSettings=@settings, shopExpirationDate=@expiration WHERE shopUrl=@url", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
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