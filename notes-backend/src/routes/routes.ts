import {Router} from 'express';
import {addNote, deleteNote, getAllNotes, modifyNote} from '../controllers/note'
const router = Router();


// The functions are defined in controllers path

/*
Path to get all notes from DB
 */
router.route('/getAllNotes').get(getAllNotes);


/*
Path to add a new note

Requires:
    req.body.description: string
 */
router.route('/addNote/:description').post(addNote);


/*
Path to modify an existing note

Requires:
    req.body.description: string
    req.body.id: number
 */
router.route('/modifyNote/:id/:description').post(modifyNote);

/*
Path to delete an existing note

Requires:
    req.body.id: number
 */
router.route('/deleteNote/:id').post(deleteNote);

export default router;
