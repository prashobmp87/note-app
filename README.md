# noteapp
Technical stack used is Jav17, SpringBoot 3.2.2 and MongoDB

Below are the APIs hosted 
1.Save the Note
    URL:http://localhost:9091/note/save
    HTTP Method: POST
    Request:
    {
        "title":"saved from docker compose",
        "text":"test",
        "tags":["PERSONAL"]   
    }
    Response:
    {
        "responseCode": 200,
        "responseMsg": "SUCCESS"
    }
2. List Notes
    URL:http://localhost:9091/note/list?tags=PERSONAL&page=0
    HTTP Method: GET
    Notes:tags is optional

3.Update Note
    URL:http://localhost:9091/note/update/{id}
     HTTP Method: PUT
     Request Body:
      {
        "title":"",
        "text":"",
        "tags":["IMPORTANT","PERSONAL"]
      }
      {
   Reponse
   {
    "responseCode": 200,
    "responseMsg": "SUCCESS"
   }
4. Delete Note
    URL:http://localhost:9091/note/delete/{id}

5. Statistics of the words in the text
     URL:http://localhost:9091/note/{id}/stat
     HTTP Method:PUT
  Response:
   {
    "responseCode": 200,
    "responseMsg": "SUCCESS",
    "data": {
        "the": 5,
        "to": 5,
        "a": 3,
        "that": 2,
        "those": 2
   }   
         
