import { Component, Input, OnInit } from '@angular/core';
import { Anuncio } from '../../interfaces/anuncio.interfaces';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'productos-prod-card',
  templateUrl: './card.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    CommonModule,
    MatButtonModule,
  ],
  styles: [],
})
export class CardComponent implements OnInit {
  @Input()
  public prod!: Anuncio;

  ngOnInit(): void {
    if (!this.prod) {
      console.warn('Producto no proporcionado al componente');
    }
  }
}
