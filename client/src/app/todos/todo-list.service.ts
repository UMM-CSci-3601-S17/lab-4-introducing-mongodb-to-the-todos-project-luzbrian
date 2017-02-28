import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Todo } from './todo';
import { Observable } from "rxjs";

@Injectable()
export class TodoListService {
    private todoUrl: string = API_URL + "todos";
    constructor(private http:Http) { }

    getTodos(): Observable<Todo[]> {
        return this.http.request(this.todoUrl).map(res => res.json());
    }

    getTodoById(id: string): Observable<Todo> {
        return this.http.request(this.todoUrl + "/" + id).map(res => res.json());
    }

    getTodoByOwner(owner: string): Observable<Todo> {
        return this.http.request(this.todoUrl+ "?owner=" + owner).map(res => res.json());
    }

    getTodoByStatus(status: string): Observable<Todo> {
        return this.http.request(this.todoUrl+ "?status=" + status).map(res => res.json());
    }

    getTodoByCategory(category: string): Observable<Todo> {
        return this.http.request(this.todoUrl+ "?category=" + category).map(res => res.json());
    }

    //Need this to work on the server side first
    //  getTodoByContains(contains: string): Observable<Todo> {
    //     return this.http.request(this.todoUrl+ "?=" + ).map(res => res.json());
    // }


}