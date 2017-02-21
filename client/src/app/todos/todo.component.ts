import { Component, OnInit } from '@angular/core';
import { TodoListService } from "./todo-list.service";
import { Todo } from "./todo";

@Component({
    selector: 'todo-component',
    templateUrl: 'todo.component.html'
})
export class TodoComponent implements OnInit {
    public todo: Todo = null;
    private _id: string;

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.getTodos();
    }

    private subscribeToServiceForId() {
        if (this._id) {
            this.todoListService.getTodoById(this._id).subscribe(
                todo => this.todo = todo,
                err => {
                    console.log(err);
                }
            );
        }
    }

    setId(id: string) {
        this._id = id;
        this.subscribeToServiceForId();
    }

    ngOnInit(): void {
        this.subscribeToServiceForId();
    }
}
