import { ComponentFixture, TestBed, async } from "@angular/core/testing";
import { Todo } from "./todo";
import { TodoListComponent } from "./todo-list.component";
import { TodoListService } from "./todo-list.service";
import { Observable } from "rxjs";
import { PipeModule } from "../../pipe.module";

describe("Todo list", () => {

    let todoList: TodoListComponent;
    let fixture: ComponentFixture<TodoListComponent>;

    let todoListServiceStub: {
        getTodos: () => Observable<Todo[]>
    };

    beforeEach(() => {
        // stub TodoService for test purposes
        todoListServiceStub = {
            getTodos: () => Observable.of([
                {
                    _id: "58895985a22c04e761776d54",
                    owner: "Blanche",
                    status: false,
                    body: "In sunt ex non tempor cillum commodo amet incididunt anim qui commodo quis. Cillum non labore ex sint esse.",
                    category: "software design"
                },
                {
                    _id: "58895985c1849992336c219b",
                    owner: "Fry",
                    status: false,
                    body: "Ipsum esse est ullamco magna tempor anim laborum non officia deserunt veniam commodo. Aute minim incididunt ex commodo.",
                    category: "video games"
                },
                {
                    _id: "58895985ae3b752b124e7663",
                    owner: "Fry",
                    status: true,
                    body: "Ullamco irure laborum magna dolor non. Anim occaecat adipisicing cillum eu magna in.",
                    category: "homework"
                }
            ])
        };

        TestBed.configureTestingModule({
            imports: [PipeModule],
            declarations: [ TodoListComponent ],
            // providers:    [ TodoListService ]  // NO! Don't provide the real service!
            // Provide a test-double instead
            providers:    [ { provide: TodoListService, useValue: todoListServiceStub } ]
        })
    });

    beforeEach(async(() => {
        TestBed.compileComponents().then(() => {
            fixture = TestBed.createComponent(TodoListComponent);
            todoList = fixture.componentInstance;
        });
    }));

    it("contains all the todos", () => {
        fixture.detectChanges();
        expect(todoList.todos.length).toBe(3);
    });

    it("contains a todo with owner 'Blanche'", () => {
        fixture.detectChanges();
        expect(todoList.todos.some((todo: Todo) => todo.owner === "Blanche" )).toBe(true);
    });

    it("contains a todo with owner 'Fry'", () => {
        fixture.detectChanges();
        expect(todoList.todos.some((todo: Todo) => todo.owner === "Fry" )).toBe(true);
    });

    it("doesn't contain a todo named 'Santa'", () => {
        fixture.detectChanges();
        expect(todoList.todos.some((todo: Todo) => todo.owner === "Santa" )).toBe(false);
    });

    it("has two todos that are incomplete", () => {
        fixture.detectChanges();
        expect(todoList.todos.filter((todo: Todo) => todo.status === false).length).toBe(2);
    });

});

        // Tried to figure out how to test angular with the filtering from mongo but couldnt understand.
// describe("Filtered Todo list", () => {
//
//     let filteredTodoList: TodoListComponent;
//     let fixture: ComponentFixture<TodoListComponent>;
//
//     let filterTodoListServiceStub: {
//         request: () => Observable<Todo[]>
//     };
//
//     beforeEach(() => {
//         // stub TodoService for test purposes
//         filterTodoListServiceStub = {
//             request: () => Observable.of([
//                 {
//                     _id: "58895985a22c04e761776d54",
//                     owner: "Blanche",
//                     status: false,
//                     body: "In sunt ex non tempor cillum commodo amet incididunt anim qui commodo quis. Cillum non labore ex sint esse.",
//                     category: "software design"
//                 },
//                 {
//                     _id: "58895985c1849992336c219b",
//                     owner: "Fry",
//                     status: false,
//                     body: "Ipsum esse est ullamco magna tempor anim laborum non officia deserunt veniam commodo. Aute minim incididunt ex commodo.",
//                     category: "video games"
//                 },
//                 {
//                     _id: "58895985ae3b752b124e7663",
//                     owner: "Fry",
//                     status: true,
//                     body: "Ullamco irure laborum magna dolor non. Anim occaecat adipisicing cillum eu magna in.",
//                     category: "homework"
//                 }
//             ])
//         };
//
//         TestBed.configureTestingModule({
//             imports: [PipeModule],
//             declarations: [ TodoListComponent ],
//             // providers:    [ TodoListService ]  // NO! Don't provide the real service!
//             // Provide a test-double instead
//             providers:    [ { provide: TodoListService, useValue: filterTodoListServiceStub } ]
//         })
//     });
//
//     beforeEach(async(() => {
//         TestBed.compileComponents().then(() => {
//             fixture = TestBed.createComponent(TodoListComponent);
//             filteredTodoList = fixture.componentInstance;
//         });
//     }));
//
//     it("contains all the todos", () => {
//         fixture.detectChanges();
//         expect(filteredTodoList.todos.length).toBe(3);
//     });
//
//     it("contains a todo with owner 'Blanche'", () => {
//         fixture.detectChanges();
//         expect(filteredTodoList.todos.some((todo: Todo) => todo.owner === "Blanche" )).toBe(true);
//     });
//
//     it("contains a todo with owner 'Fry'", () => {
//         fixture.detectChanges();
//         expect(filteredTodoList.todos.some((todo: Todo) => todo.owner === "Fry" )).toBe(true);
//     });
//
//     it("doesn't contain a todo named 'Santa'", () => {
//         fixture.detectChanges();
//         expect(filteredTodoList.todos.some((todo: Todo) => todo.owner === "Santa" )).toBe(false);
//     });
//
//     it("has two todos that are incomplete", () => {
//         fixture.detectChanges();
//         expect(filteredTodoList.todos.filter((todo: Todo) => todo.status === false).length).toBe(filteredTodoList.todos.filter((todo: Todo) => todo.status === false).length);
//     });
//
// });