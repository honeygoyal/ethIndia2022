import React, { Component, useState } from 'react'
import './CreateApp.css';
import swal from 'sweetalert';
import axios from 'axios';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import propsData from './prop.json'
import { useContractMethod } from "./hooks";
function CreateApp() {

  const { state: create, send: setCreate,receipt } =
    useContractMethod("create");

    
  const [apkFiles, setApkFiles] = useState();
  const [coverImageFiles, setCoverImageFiles] = useState();
  const [appIconFiles, setAppIconFiles] = useState();
  const [screenshotFiles, setScreenshotFiles] = useState([]);
  const typeData = [
    { Type: 'App', TypeId: '2' },
    { Type: 'Games', TypeId: '1' },
    { Type: 'Comics', TypeId: '3' }
  ];
  const categoryData = [
    { categoryName: 'ABC', TypeId: '1' },
    { categoryName: 'DEF ', TypeId: '2' },
    { categoryName: 'GHI ', TypeId: '3' }
  ];
  const [categoryDataToDisplay, setCategoryDataToDisplay] = useState([]);
  const selectCategory = (event) => {
    typeChangeHandler(event);
    const typeSelected = typeData.filter(type => {
      if (type.Type == event.target.value) {
        return type;
      }
    })
    let categoryDataToshow = [];
    categoryDataToshow = categoryData.filter(category => {
      if (category.TypeId === typeSelected[0].TypeId) {
        return category
      }
    })
    setCategoryDataToDisplay(categoryDataToshow);
  };
  var formdata = new FormData();
  const handleSubmit = e => {
    e.preventDefault()
    formdata.append("appName", appName);
    formdata.append("appDesc", appDescription);
    formdata.append("language", language);
    formdata.append("type", type.toLowerCase());
    formdata.append("category", category.toLowerCase());
    formdata.append("subscription", subscription);
    formdata.append("apk", apkFiles);
   // formdata.append("cover_image", coverImageFiles);
    formdata.append("appIcon", appIconFiles);
    formdata.append("screenshots1", screenshotFiles[0]);
    formdata.append("screenshots2", screenshotFiles[1]);
    formdata.append("screenshots3", screenshotFiles[2]);
    axios
      .post(propsData.CREATE_APP_URL, formdata, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        
        setCreate(res.data.category,res.data.appType,res.data.appName,res.data.dataCid,res.data.appIconCid)
        if(receipt.status){

          swal("App Created Successfully", receipt.transactionHash);
        }
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    setAppName('')
    setAppDescription('')
    setLanguage('')
    setType('')
    setCategory('')
    setAppIcon('')
    setSubscriptionType('')
    setCoverImage('')
    setScreenshot('')
    setApkFile('')
  }

  const [appName, setAppName] = useState('');
  const [appDescription, setAppDescription] = useState('');
  const [language, setLanguage] = useState('');
  const [type, setType] = useState('');
  const [category, setCategory] = useState('');
  const [appIcon, setAppIcon] = useState('');
  const [subscription, setSubscriptionType] = useState('');
  const [coverImage, setCoverImage] = useState('');
  const [screenshot, setScreenshot] = useState('');
  const [apkFile, setApkFile] = useState('');

  const apkFileChangeHandler = (event) => {
    setApkFiles(event.target.files[0])
    setApkFile(event.target.value);
  };
  const languageChangehnadler = (event) => {
    setLanguage(event.target.value);
  };
  const screenshotChangeHandler = (event) => {
    let files = [event.target.files[0], event.target.files[1], event.target.files[2]]
    setScreenshotFiles(files);
    setScreenshot(event.target.value);
  };
  const coverImageChangeHandler = (event) => {
    setCoverImageFiles(event.target.files[0]);
    setCoverImage(event.target.value);
  };
  const subscriptionChangeHandler = (event) => {
    setSubscriptionType(event.target.value);
  };
  const appIconChangeHandler = (event) => {
    formdata.append("app_icon", event.target.files[0]);
    setAppIconFiles(event.target.files[0]);
    setAppIcon(event.target.value);
  };
  const categoryChangeHandler = (event) => {
    setCategory(event.target.value);
  };
  const appNameChangeHandler = (event) => {
    setAppName(event.target.value);
  };

  const appDescriptionChangeHandler = (event) => {
    setAppDescription(event.target.value);
  };

  const typeChangeHandler = (event) => {
    setType(event.target.value);
  };

  return (
    <>
      <div className='createAppForm'>
        <Card style={{ width: '50rem' }}>
          <Card.Body >
            <Card.Title className="title" >Create App Form</Card.Title>
            
            <Form onSubmit={handleSubmit} className="formContent mr-5">
              <Form.Group className="mb-3" controlId="formAppName">
                <Form.Label>App Name</Form.Label>
                <Form.Control required value={appName} onChange={appNameChangeHandler} type="text" placeholder="App Name" />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formAppDescription">
                <Form.Label>App Description</Form.Label>
                <FloatingLabel controlId="floatingTextarea2" label="App Description">
                  <Form.Control
                    required
                    as="textarea"
                    placeholder=""
                    style={{ height: '100px' }}
                    value={appDescription} onChange={appDescriptionChangeHandler}
                  />
                </FloatingLabel>
              </Form.Group>
              <Form.Group controlId="formLanguage" className="mb-3">
                <Form.Label>Language</Form.Label>
                <Form.Select required value={language} onChange={languageChangehnadler} aria-label="Type">
                  <option disabled value="">Select Language</option>
                  <option value="English">English</option>
                  <option value="French">French</option>
                  <option value="German">German</option>
                </Form.Select>
              </Form.Group>

              <Form.Group controlId="formType" className="mb-3">
                <Form.Label>Type</Form.Label>
                <Form.Select defaultValue={'Comics'} required value={type} onChange={selectCategory} aria-label="Type">
                  <option disabled value="">Select Type</option>
                  {typeData.map((type) => {
                    return <option value={type.Type}>{type.Type}</option>
                  }
                  )}
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formCategory" className="mb-3">
                <Form.Label>Category</Form.Label>
                <Form.Select required value={category} onChange={categoryChangeHandler} aria-label="Category">
                  <option disabled value="">Select Cateory</option>
                  {categoryDataToDisplay.map((category) => {
                    return <option value={category.categoryName}>{category.categoryName}</option>
                  }
                  )}
                </Form.Select>
              </Form.Group>

              <Form.Group controlId="formAppIcon" className="mb-3">
                <Form.Label>App Icon</Form.Label>
                <Form.Control accept='image/png, image/jpg, image/jpeg' required value={appIcon} onChange={appIconChangeHandler} type="file" />
              </Form.Group>
              <Form.Group controlId="formSubscriptionType" className="mb-3">
                <Form.Label>Subscription Type</Form.Label>
                <Form.Select required value={subscription} onChange={subscriptionChangeHandler} aria-label="Subscription Type">
                  <option disabled value="">Select Subscription</option>
                  <option value="free">Free</option>
                  <option value="paid">Paid</option>
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formCoverImage" className="mb-3">
                <Form.Label>Cover Image</Form.Label>
                <Form.Control accept='image/png, image/jpg, image/jpeg' required value={coverImage} onChange={coverImageChangeHandler} type="file" />
              </Form.Group>
              <Form.Group controlId="formScreenshots" className="mb-3">
                <Form.Label>Screenshot Image</Form.Label>
                <Form.Control accept='image/png, image/jpg, image/jpeg' required value={screenshot} onChange={screenshotChangeHandler} type="file" multiple="true" />
              </Form.Group>
              <Form.Group controlId="formApkFile" className="mb-3">
                <Form.Label>APK File</Form.Label>
                <Form.Control accept='.apk' required value={apkFile} onChange={apkFileChangeHandler} type="file" />
              </Form.Group>
              <Button variant="primary" type="submit">
                Submit
              </Button>
            </Form>
          </Card.Body>
        </Card>
      </div>
    </>
  )
}
export default CreateApp;