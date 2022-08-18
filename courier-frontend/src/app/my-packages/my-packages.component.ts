import { Component, OnInit } from '@angular/core';
import { Observable, Observer } from 'rxjs';
import { Address } from '../model/address.model';
import { Package } from '../model/package.model';
import { Receiver } from '../model/receiver.model';
import { Sender } from '../model/sender.model';

@Component({
  selector: 'app-my-packages',
  templateUrl: './my-packages.component.html',
  styleUrls: ['./my-packages.component.css']
})
export class MyPackagesComponent implements OnInit {
  counter = 1;
  // packages from async call - mock
  packages$ = new Observable((observer: Observer<Package[]>) => {
    observer.next(
      [
        new Package('8a24bf10-d250-4a01-85d7-03edd9388bc5', '7399e12d6a5b49a6adf004dc2f9c2aa9', 'computer peripherals', 'NEW', 5.8, true, false, 3, new Sender('John', 'Doe', '0068742655', 'johnd@example.com', new Address('None st.', '4', 'London', '24', 'LTX-06E', 'United Kingdom'), 'Company LTD'), new Receiver('Tom', 'Johnson', '706-974-4473', 'tom.johnson@example.com', new Address('Davis Street', '4175', 'Dawsonville', '1', 'GA30534', 'Georgia'))),
        new Package('b7627312-d9d5-4689-baba-ecedc4f3e50a', '6d6cb9052f2e49a8a1e7845ed381cd6e', 'car headlight', 'NEW', 16.1, true, false, 1, new Sender('Johnny', 'Bravo', '206-441-6794', 'johhny_handsome@example.com', new Address('University st.', '48', 'London', '6', '98121', 'Washington, US')), new Receiver('Tom', 'Jeff', '812-710-9149', 'tom.j@example.com', new Address('Turkey Pen Lane', '91', 'Montgomery', '1', 'GA30534', 'Alabama, US'))),
        new Package('1ef573ef-beda-4214-8d45-133580af1bbb', '450f4076b5164badbe36e0a98c62a54f', 'clothes', 'NEW', 28.9, false, false, 2, new Sender('Reddy', 'Jung', '+492651565', 'info@example.com', new Address('Main Road', '79', 'London', '3', 'NW84 5KB', 'UK')), new Receiver('Jacob', 'Kronen', '+449822166326', 'jacobkronen@example.com', new Address('Stanley Road', '21', 'London', '38', 'E36 7WJ', 'UK')))
      ]
    );
    observer.complete();
  });




  constructor() { }

  ngOnInit(): void {
  }

  getCounter(): number{
    return this.counter++;
  }

}
