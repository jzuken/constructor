namespace RepoLibrary
{
    public interface IDatabaseGateway
    {
        Project GetProject(string url);

        string SaveProject(Project data);
    }
}
