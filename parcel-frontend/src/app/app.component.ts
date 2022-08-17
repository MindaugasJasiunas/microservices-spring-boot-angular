import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Address } from './model/address.model';
import { Package } from './model/package.model';
import { Receiver } from './model/receiver.model';
import { Sender } from './model/sender.model';
import { AuthenticationService } from './service/authentication.service';
import { ParcelService } from './service/parcel.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'parcel-frontend';

  constructor(/*private authService: AuthenticationService, parcelService: ParcelService*/) {
    // authService.login({username: 'useris', password: 'password'}).subscribe({
    //   next: (data) => {
    //     console.log(data);
    //     console.log(data.headers);
    //     console.log(data.headers.get('authorization'));
    //     console.log(data.headers.get('Authorization'));
    //   },
    //   error: (err: Error) => {
    //     console.log(err);
    //     console.error(err.message);
    //   },
    // });

    // authService
    //   .register({
    //     username: 'pamella',
    //     password: 'password',
    //     firstName: 'Pam',
    //     lastName: 'Jacobs',
    //   })
    //   .subscribe({
    //     next: (data) => {
    //       console.log(data);
    //       console.log(data.headers);
    //     },
    //     error: (err: Error) => {
    //       console.log(err);
    //       console.log(err.message);
    //     },
    //   });

    // parcelService
    //   .trackParcel('c2b22008684844e2ae2473f178fed749')
    //   .subscribe({
    //     next: (data: Package) => {
    //       console.log(data);
    //     },
    //     error: (err: HttpErrorResponse) => {
    //       console.log(err);
    //       console.log(err.error.message);
    //     }
    //   })

    // parcelService
    //   .getParcels()
    //   .subscribe({
    //     next: (data) => {
    //       console.log(data.content as Package[]);
    //     },
    //     error: (err: HttpErrorResponse) => {
    //       console.log(err);
    //       console.log(err.error.message);
    //     }
    //   })

    // parcelService
    //   .getParcels2(1,2)
    //   .subscribe({
    //     next: (data) => {
    //       console.log(data.content as Package[]);
    //     },
    //     error: (err: HttpErrorResponse) => {
    //       console.log(err);
    //       console.log(err.error.message);
    //     }
    //   })

    // let addressSender = new Address('None st.', "14", "London", "1", "LXY 06E", "United Kingdom");
    // let addressReceiver = new Address('Backway st.', "3", "Texas", "78", "T65515", "United States");
    // let pkg = new Package('9d909df3-9469-4a48-9082-cbc6748d3644', 'scjkbsdfnwnfun54', "desc", "NEW", 15, true, false, 2, new Sender('John', 'Doe', '0043581989', 'mail@example.com', addressSender), new Receiver('Tom', 'Hanks', '887546512311', 'tom@example.com', addressReceiver));
    // parcelService.createNewParcel(pkg).subscribe({
    //   next: (data: HttpResponse<any>)=>{
    //     console.log(data);
    //     console.log(data.status); // 200
    //     console.log(data.statusText); // 'OK'
    //   },
    //   error: (err: HttpErrorResponse)=> {
    //     console.log(err);
    //     console.log(err.error.message);
    //   }
    // });




  }
}

