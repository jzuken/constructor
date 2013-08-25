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

        public Project GetProject(string name)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlDataReader myReader = null;
            SqlCommand myCommand = new SqlCommand("select * from shops where shopName=@name", connection);
            myCommand.Parameters.Add("@name", SqlDbType.VarChar).Value = name;
            myReader = myCommand.ExecuteReader();
            while (myReader.Read())
            {
                Console.WriteLine(myReader["shopName"].ToString());
                Console.WriteLine(myReader["shopSettings"].ToString());
                Project project = new Project();
                project.Settings = myReader["shopSettings"].ToString();
                project.Name = myReader["shopName"].ToString();
                connection.Close();
                return project;
            }
            connection.Close();
            return null;
        }

        public string SaveProject(Project data)
        {
            Project project = GetProject(data.Name);
            if (project == null)
            {
                SqlConnection connection = new SqlConnection(this.connectionString);
                connection.Open();
                SqlCommand myCommand = new SqlCommand("INSERT INTO shops (shopName, shopSettings) " +
                    "Values (@name,@settings)", connection);
                myCommand.Parameters.Add("@name", SqlDbType.VarChar).Value = data.Name;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
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
                SqlCommand myCommand = new SqlCommand("UPDATE shops SET shopSettings=@settings WHERE shopName=@name", connection);
                myCommand.Parameters.Add("@name", SqlDbType.VarChar).Value = data.Name;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
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