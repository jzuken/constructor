namespace RepoLibrary
{
    public interface IDatabaseHander
    {
        Project GetProject(int id);

        string SaveProject(Project data);
    }
}
