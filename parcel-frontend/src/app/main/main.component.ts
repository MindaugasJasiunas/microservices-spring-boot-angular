import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  submit(form: NgForm){
    if(form.status === 'VALID'){
      const trackingNumber = (form.value as {"trackingNumber": string}).trackingNumber;
      this.router.navigate(['/tracking/'+trackingNumber]);
    }
  }

}
