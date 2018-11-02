import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../core-module/services/shopping-list.service";
import { BackendShoppingListService } from "./services/backend-shopping-list.service";
import { SwPush } from "@angular/service-worker";
import { UserService } from "../core-module/services/user.service";

@Component({
    templateUrl: './user-root.component.html',
    styleUrls: ['./user-root.component.css'],
    providers: [{
        provide: ShoppingListService,
        useClass: BackendShoppingListService
    }]
})
export class UserRootComponent implements OnInit {

    constructor(
        private swPush: SwPush,
        private userService: UserService
    ) {
    }

    ngOnInit() {

        this.swPush.requestSubscription({
            serverPublicKey: "BArkB_1gSLGOPruj4ZC3HeRmq9ncz4vKrDomlNXpOYEeJircD7VPXCY-3Px4MjzhvocOWFmW2c9zy9HHKKbTE4Q"
        }).then(subscription => {
            this.userService.sendPushSubscription(subscription).subscribe(() => {
                console.log('subscription sent');
            });
        });
    }

}
