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
    public owners: Todo[];
    public category: Todo[];

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.getTodos();
    }

    ngOnInit(): void {
        this.todoListService.getTodos().subscribe(
            todos => this.todos = todos,
            err => {
                console.log(err);
            }
        );

        //   this.todoListService.getTodoByOwner().subscribe(
        //     owners => this.owners = owners,
        //     err => {
        //         console.log(err);
        //     }
        // );
        //
        // this.todoListService.getTodoByCategory().subscribe(
        //     category => this.category = category,
        //     err => {
        //         console.log(err);
        //     }
        // );
    }
}