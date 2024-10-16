import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { CommonModule } from '@angular/common';
import { Anuncio } from '../../interfaces/anuncio.interfaces';
import { CardComponent } from '../../components/card/card.component';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CardComponent, MatCardModule, MatDividerModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [DataService],
})
export class HomeComponent {
  public prod: Anuncio[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getAnuncios().subscribe(
      (prod) => {
        console.log('Datos recibidos:', prod); // Para verificar la llegada de datos
        this.prod = prod;
      },
      (error) => console.error('Error en la carga de datos:', error)
    );
  }

  //  ngOnInit(): void {
  //      this.dataService.getAnuncios().
  //      subscribe(prod => this.prod = prod);
  //  }
}
