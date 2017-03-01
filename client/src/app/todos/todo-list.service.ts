import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Todo } from './todo';
import { Observable } from "rxjs";

@Injectable()
export class TodoListService {
    private todoUrl: string = API_URL + "todos";

    constructor(private http: Http) {
    }

    getTodos(owner: string, status: string, category: string): Observable<Todo[]> {

        if (owner) {
            this.getTodoByOwner(owner);
        }

        if (status) {
           this.getTodoByStatus(status);
        }

        if (category) {
            this.getTodoByCategory(category);
        }

        return this.http.request(this.todoUrl).map(res => res.json());
    }

    getTodoById(id: string): Observable<Todo> {
        return this.http.request(this.todoUrl + "/" + id).map(res => res.json());
    }


    getTodoByOwner(owner: string): Observable<Todo> {
        if (owner) {
            return this.http.request(this.todoUrl + "?owner=" + owner).map(res => res.json());
        }
    }

    getTodoByStatus(status: string): Observable<Todo> {
        if (status) {
            return this.http.request(this.todoUrl + "?status=" + status).map(res => res.json());
        }
    }

    getTodoByCategory(category: string): Observable<Todo> {
        if (category) {
            return this.http.request(this.todoUrl + "?category=" + category).map(res => res.json());
        }
    }
}