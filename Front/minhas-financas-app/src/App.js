import React from 'react';
import './App.css';

class App extends React.Component {
 
  state = {
    numero1 : null,
    numero2 : null,
    resultado: null
  }

  somar = () => {
    const soma = parseInt(this.state.numero1) + parseInt(this.state.numero2)
    this.setState({resultado: soma})  
  }

  subtrair = () => {
    const subtr = parseInt(this.state.numero1) - parseInt(this.state.numero2)
    this.setState({resultado: subtr })
  }

  render(){
    return (
      <div >
        <label>Numero1: </label>
        <input type="text" value={this.state.numero1} 
                onChange={(e) => this.setState({numero1: e.target.value}) } />
        <br/>
        <label>Numero2: </label>
        <input type="text" value={this.state.numero2} 

                onChange={(e) => this.setState({numero2: e.target.value}) } />
      <br/>
      <button onClick={this.somar} >Somar</button>
      <br/>
      <button onClick={this.subtrair} >Subtrair</button>

      <br/>
      O resultado: {this.state.resultado}


      </div>
    )
  }
}

export default App;
