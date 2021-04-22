
import {Request, Response} from 'express';
import * as mysql from 'mysql';

const connection = mysql.createConnection({
   host: "localhost",
   user: "diego",
   password: "admin12345",
   database: "notes"
});


// Function to get all the categories from mongo db
export async function getAllNotes(req: Request, res: Response) {

   connection.query('select * from notes;', function (error, results, fields) {
      if (error) throw error;
      res.json(results);
   });

}

// Function to add a new note
export async function addNote(req: Request, res: Response) {
   connection.query(`insert into notes(description) values('${req.params.description}');`, function (error, results, fields) {
      if (error) throw error;
      res.json(results);
   });

}

// Function to modify a existing note
export async function modifyNote(req: Request, res: Response) {
   connection.query(`update notes set description = '${req.params.description}' where id = ${req.params.id};`, function (error, results, fields) {
      if (error) throw error;
      res.json(results); 
   });

}

// Function to delete a existing note
export async function deleteNote(req: Request, res: Response) {
   connection.query(`delete from notes where id = ${req.params.id};`, function (error, results, fields) {
      if (error) throw error;
      res.json(results);
   });

}
