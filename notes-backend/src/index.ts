import app from './app';

async function main()
{
    // const db = await connect(); // Db connect
    await app.listen(app.get("port"));
    console.log("server on port", app.get("port"));
}

main();
