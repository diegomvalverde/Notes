export const environment = {
    // Server  properties
    SERVER_BASE_DIR: "",
    SERVER_PORT: 3080, // Needs to be type number

    // Mongo properties for categories, brands, etc
    DB_HOST: "localhost",
    DB_PORT: "3306", // It is specified inside mongo host
};

export function getDBURL()
{
    return `${environment.DB_HOST}:${environment.DB_PORT}`
}
