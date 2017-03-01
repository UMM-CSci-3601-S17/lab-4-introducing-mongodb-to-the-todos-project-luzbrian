import { Component, OnInit } from '@angular/core';
import { TodoListService } from "./todo-list.service";
import { Todo } from "./todo";
import { FilterBy } from "../users/filter.pipe";

@Component({
    selector: 'todo-list-component',
    templateUrl: 'todo-list.component.html',
    providers: [ FilterBy ]
})

export class TodoListComponent {
    public todos: Todo[];
    public filterOwner : string = "";
    public filterCategory : string = "";
    public filterBody : string = "";
    public filterStatus : string = "";
    public sortBy : string = "";
    private freakingOut : boolean = false;

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.getTodos();
    }

    ngOnInit(): void {
        //     this.todoListService.getTodos().subscribe(
        //         todos => this.todos = todos,
        //         err => {
        //             console.log(err);
        //         }
    };


    private requestData(owner: string, category: string, status: string, body: string): string {
        var parameter1 = new Array();
        var searchAdd = "?";
        var parameters = "";


        if (owner != "")
            parameter1["owner"] = owner;

        if (category != "")
            parameter1["category"] = category;

        if (status != "")
            parameter1["status"] = status;

        if (body != "")
            parameter1["body"] = body;


        /**
         * Construct params string by the value of the elements of which are enabled
         */
        for (let i = 0; i < parameter1.length; i++) {
            parameters = parameters + searchAdd + i + "=" + parameter1[i]
            searchAdd = "&"
        }

        // for (var p in parameter1) {
        //     parameters += search + p + "=" + parameter1[p]; //Construct ?owner=Name&limit=3 etc.
        //     search = "&";
        // }
        return parameters;
    }

    public request(owner: string, category: string, status: string, body: string) {
        var req: string = this.requestData(owner, category, status, body);
        this.todoListService.getTodos(owner, status, category).subscribe(
            todos => this.todos = todos,
            err => {
                console.log(err);
            }
        );


    }
}