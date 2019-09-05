import React, { Component } from "react";
import axios from 'axios'
/* Import Components */
import Input from "./Input";
import Button from "./Button";

const instance = axios.create({
  baseURL: 'http://localhost:8080/benovative/employee',
  headers: {'Content-Type': 'application/json'}
});


class FormContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      employee: {
        firstName: "",
        lastName: "",
        email: ""
      },
      employeeList: [],
      newEmail: ""
    };
    this.handleNewEmail = this.handleNewEmail.bind(this);
    this.handleFormSubmit = this.handleFormSubmit.bind(this);
    this.handleDeleteByEmail = this.handleDeleteByEmail.bind(this);
    this.handleUpdate = this.handleUpdate.bind(this);
    this.handleInput = this.handleInput.bind(this);
    this.handleFindByEmail = this.handleFindByEmail.bind(this);
    this.handleFindAll = this.handleFindAll.bind(this);
  }

  /* Handlers */

  handleNewEmail(e) {
    let value = e.target.value;
    this.setState(
      prevState => ({
        employee: {
          ...prevState.employee
        },
        newEmail: value
      })
    );
  }

  handleInput(e) {
    let value = e.target.value;
    let name = e.target.name;
    this.setState(
      prevState => ({
        employee: {
          ...prevState.employee,
          [name]: value
        }
      })
    );
  }

  handleFormSubmit(e) {
    e.preventDefault();
    let employeeData = this.state.employee;

    instance.post('', employeeData)
    .then(response => 
      {
        this.handleFindAll(e);
      })
    
  }

  handleUpdate(e) {
    e.preventDefault();
    let employeeData = this.state.employee;
    let newEmail = this.state.newEmail;

    instance.put('', employeeData, { 
      params: {
        newEmail: newEmail
      }})
    .then(response => 
      {
        this.handleFindAll(e);
      })
    
  }

  handleDeleteByEmail(e) {
    e.preventDefault();
    let email = this.state.employee.email;
    instance.delete('', {
      params: {
        email: email
      }
    })
    .then(response => 
      {
        this.handleFindAll(e);
      })
  }

  handleFindAll(e) {
    e.preventDefault();
    instance.get('')
    .then(response => 
      {
        console.log(response.data);

        this.setState({
          employeeList: response.data.map((employee, i) => (
            <li key={i} className="list-group-item">{employee.firstName} {employee.lastName} - {employee.email}</li>
          ))
  
        });
      })
  }

  handleFindByEmail(e) {
    e.preventDefault();
    let email = this.state.employee.email;
    instance.get('', {
      params: {
        email: email
      }
    })
    .then(response => 
      {
        this.setState({
          employee: response.data
        })
      })
  }

  /* Render */
  render() {
    return (
      <div>
        <form className="container-fluid" onSubmit={this.handleFormSubmit}>
          <Input
            inputType={"text"}
            title={"First Name: "}
            name={"firstName"}
            value={this.state.employee.firstName}
            placeholder={"First name"}
            handleChange={this.handleInput}
          />{" "}
          <Input
            inputType={"text"}
            title={"Last Name: "}
            name={"lastName"}
            value={this.state.employee.lastName}
            placeholder={"Last name"}
            handleChange={this.handleInput}
          />{" "}
          <Input
            inputType={"text"}
            title={"Email Address: "}
            name={"email"}
            value={this.state.employee.email}
            placeholder={"Email"}
            handleChange={this.handleInput}
          />{" "}
          <Input
            inputType={"text"}
            title={"New Email Address: "}
            name={"newEmail"}
            value={this.state.employee.newEmail}
            placeholder={"New Email"}
            handleChange={this.handleNewEmail}
          />{" "}
          <Button
            action={this.handleFormSubmit}
            type={"primary"}
            title={"Save"}
            style={buttonStyle}
          />{" "}
          <Button
            action={this.handleUpdate}
            type={"secondary"}
            title={"Update"}
            style={buttonStyle}
          />{" "}
          <Button
            action={this.handleDeleteByEmail}
            type={"secondary"}
            title={"Delete By Email"}
            style={buttonStyle}
          />{""}
          <br/>
          <Button
            action={this.handleFindByEmail}
            type={"secondary"}
            title={"Find By Email"}
            style={buttonStyle}
          />{" "}
          <Button
            action={this.handleFindAll}
            type={"secondary"}
            title={"Find All"}
            style={buttonStyle}
          />{" "}
        </form>
      
        <div>
          <ul className="list-group list-group-flush">
            {this.state.employeeList}
          </ul>
        </div>
 
      </div>
    );
  }
}

const buttonStyle = {
  margin: "10px 10px 10px 10px"
};

export default FormContainer;
