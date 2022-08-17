import { Component } from '@angular/core';
import { AuthenticationService } from './service/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'parcel-frontend';

  constructor(private authService: AuthenticationService) {
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
  }
}
