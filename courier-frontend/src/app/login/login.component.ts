import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  form: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });
  private subscriptions: Subscription[] = [];
  private error: string | null = null;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    // check if logged in trying to access login page
    if (this.authenticationService.isLoggedIn()) {
      console.log('isloggedin');
      this.router.navigate(['/']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  ngOnDestroy(): void {
    // remove all subscriptions when destroying component
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  submit() {
    if (this.form.valid) {
      // this.submitEM.emit(this.form.value);
      this.subscriptions.push(
        this.authenticationService
          .login({
            username: this.form.get('username')?.value,
            password: this.form.get('password')?.value,
          })
          .subscribe({
            next: (response: HttpResponse<any>) => {
              // get refresh token
              this.authenticationService.saveRefreshToken(
                response.headers.get('authorization')!
              );
              // sets username to localStorage
              this.authenticationService.isLoggedIn();
              // get access token
              this.subscriptions.push(
                this.authenticationService
                .getAccessToken(response.headers.get('authorization')!)
                .subscribe({
                  next: (response: HttpResponse<void>) => {

                    // check if courier when logging-in
                    if (!this.authenticationService.isUserCourier(response.headers.get('authorization')!)){
                      this.router.navigate(['/']);
                    }

                    this.authenticationService.saveAccessToken(
                      response.headers.get('authorization')!
                    );
                    // redirect to main page
                    this.router.navigate(['/']);
                  },
                  error: (err: HttpErrorResponse) => {
                    this.error = err.error.message;
                  },
                })
              );
            },
            error: (err: HttpErrorResponse) => {
              this.error = err.error.message;
            }
          })
      );
    }
  }

  showError(): string | null {
    if (this.error) {
      return this.error;
    } else if (
      (!this.form.get('username')?.valid &&
        this.form.get('username')?.touched) ||
      (!this.form.get('password')?.valid &&
      this.form.get('password')?.touched)
    ) {
      return 'Username or password invalid';
    }
    return null;
  }
}
