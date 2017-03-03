import { Component, OnInit } from '@angular/core';
import { TodoListService } from "./todo-list.service";
import { Todo } from "./todo";
import { FilterBy } from "../users/filter.pipe";

@Component({
    selector: 'todo-list-component',
    templateUrl: 'todo-list.component.html',
    providers: [ FilterBy ]
})

export class TodoListComponent implements OnInit {
    public todos: Todo[];
    public searchOwner: string = "";
    public searchCategory: string = "";
    public searchStatus: string = "";

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.getTodos();
    }

    ngOnInit(): void {
            this.todoListService.getTodos().subscribe(
                todos => this.todos = todos,
                err => {
                    console.log(err);
                })
    };

    private requestData(owner: string, category: string, status: any): string {
        let holder = Array();
        let searchAdd = "?";
        let parameters = "";


        if (owner != "")
            holder["owner"] = owner;

        if (category != "")
            holder["category"] = category;

        if (status != "")
            holder["status"] = status;

        for (let param in holder) {
            parameters = parameters + searchAdd + param  + "=" + holder[param];
            searchAdd = "&";
        }

        return parameters;
    }

    public request(owner: string, category: string, status: any) {
        let requestParam: string = this.requestData(owner, category, status);
        this.todoListService.getFilteredTodos(requestParam).subscribe(
            todos => this.todos = todos,
            err => {
                console.log(err);
            }
        );


    }
}